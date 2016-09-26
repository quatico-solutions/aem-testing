# AemTesting Library

A library for efficient creation of unit and integration tests in the AEM technology stack.
The library provides creation methods for JCR resources that can be easily customized 
to create your components, pages, assets, tags and services for your web application.

Smart builders allow for concise test setups with valid default values. Direct service injection
provides your services as mocked or actual implementation. Standard Sling adapter 
registration makes custom implementations available.


## Getting Started

1. Provide a `UnitTestBase` class as parent of all your unit tests.
This class provides the testing API to your tests and binds it to the ``$`` character.  
	 ```java
	 public class UnitTestBase { 
		protected ITestSetup $;
		private IAemClient client; 
		
		@Before 
		public void setUpTestContext() throws Exception {     
			this.client = new AemClient(Type.Unit).startUp();  // Type.Integration for integrated tests
			IResources resources = new Resources(client);     
			$ = SetupFactory.create(ITestSetup.class).getSetup(
					client, 
					resources, 
					new Pages(client), 
					new Components(resources, client),                                
					new Assets(client)); 
		} 
		
		@After public void tearDownTestContext() throws Exception { 
			this.client.shutDown();   
		}
	}
	 ```
Here we create an ``AemClient`` with a ``ResourceResolverType``. Currently there are two valid 
options: ``Type.Unit`` and ``Type.Integration``. We create a ``ITestSetup`` with  creation methods 
for ``Resources``, ``Pages``, ``Components`` and ``Assets``. Skip to 
[How to create your own testing API](How to create your own testing API) if you want to see how to 
add your own project specific implementors.

2. Now with the test setup available, let's add a first unit test. For example to test some Java 
code that needs a JCR resource on a page, create your test class and extend `UnitTestBase`. Then 
call your API as follows: 
	```java
	public class TextImageControllerTest extends UnitTestBase {
	@Test public void isShowTitleBelowWithImagePresentAndSizeSmallAndPositionLeftReturnFalse() throws Exception {   
		Resource page   = $.aPage("/content/test/ko/home/page");   
		Resource target = $.aResource(page, "/jcr:content/foobar", "text", "<b>hello</b>");     
		
		// your code using the resources comes here followed by assertions
	}
	```


## How to create your own testing API

1. Let's add our project specific creation/builder methods: We want to add API to easily create the 
resource `TeaserConfiguration`.
	```java
	public class AppComponents extends Components implements IAppComponents { 
		AppComponents(IResources resources, IAemClient client) {
			this.resources = resources;
			this.client = client;
		}
		public Resource aTeaserConfiguration(Resource parent, TeaserType type, Object... p) {  
			String configPath = type != null ? type.getConfigPath() : PageTeaser.PATH_EXTENSION;  
			Properties props = new Properties(p).append("sling:resourceType", ComponentType.TEASER_CONFIGURATION);
			return resources.aResource(parent, configPath, props.toArray());
		}
	}
	```

2. Now you want to inherit from `IAppComponent` in `ITestSetup`.
	```java
	public interface ITestSetup extends IAemClient, IResources, IPages, IAppComponents, IAssets {}
	```

3. Finally extend your `UnitTestBase` so the new method can be called directly by 
`$.aTeaserConfiguration(...)` within the tests.
	```java
	...
	IResources resources = new Resources(client);
	$ = SetupFactory.create(ITestSetup.class).getSetup(
			client, 
			resources, 
			new Pages(client),                                
            new Assets(client), 
            new AppComponents(resources, client)); 
	...
	```
	
Make sure to follow some basic conventions to stay consistent with the provided API:
* Creation/builder methods that start with `a...` take following arguments: **Parent resource, 
relative path (name of resource) and properties**.
  ```java
  public Resource aText(Resource parent, String relative, Object... properties) throws Exception;
  ```
* Creation/builder methods that go with the structure `a...WithParent` take following arguments: *Absolute path, properties*.
  ```java
  public Resource aConfigPageWithParents(String path, Object... properties) throws Exception;
  ```
* Creation/builder methods that take no arguments return `AbstractBuilder<T>`.
  ```java
  ComponentContextBuilder aComponentContext();
  ```


## Details on builders, services and adapters


### Build your own builder
AemTesting makes it easy to add your own builder implementations. 

1. Simply extend `AbstractBuilder<T>` with your builder class that builds `T`. Let's create an AssetBuilder:
	```java
	public class AssetBuilder extends AbstractBuilder<Asset> {
		public AssetBuilder() {
			super(Asset.class);
		}
		
		@Override
		protected Asset internalBuild() throws Exception {
			result = // create your mocked or actual asset implementation
		    return result;
		}
	}
	```
	Make sure to override `internalBuild()` because that is where we build our `Asset` eventually.

2. We want to create an `Asset` with **path** and a **mimetype**. The latter is an optional property 
thus we provide a default value. A builder can be implemented as follows:
	```java
	public class AssetBuilder extends AbstractBuilder<Asset> {
        public AssetBuilder(IAemClient client) {
       	    this.client = client;
       	    mimetype("image/jpeg");
        }
		public String path() {
            return getValue("path", String.class);
        }
        
        public AssetBuilder path(String value) {
            return addValue("path", value);
        }
        
        public String mimetype() {
            return getValue("mimetype");
        }
        
        public AssetBuilder mimetype(String value) {
            return addValue("mimetype", value);
        }
       
         ...
        
        @Override
        protected Asset internalBuild() throws Exception {
       		ContentBuilder content = client.getContentBuilder();
       		this.result = content.asset(path, classpathResource(), mimetype(), metadata());
      		return result;
        }
    }
	```
	Make sure to set all optional values in the constructor in case the setters are never called.
	

### Provide your own services
Use the following API from IAemClient to create mocked or actual service implementations and 
inject them into your container.
```java
<R> R require(Class<R> serviceClass, Object... properties) throws Exception;

<R> R require(InjectedService<R> service, Object... properties) throws Exception;

<R> R require(IServiceBuilder<R> builder, Object... properties) throws Exception;

<R, C extends R> InjectedService<R> service(Class<R> type, C service, InjectedService<?>... requiredServices) throws Exception;

<R> InjectedService<R> service(R service, InjectedService<?>... requiredServices) throws Exception;

<R> InjectedService<R> service(IServiceBuilder<R> builder, InjectedService<?>... requiredServices) throws Exception;
```
The following tests demonstrate how the API can be used:
```java
@Test
public void viewGetTitleWithoutAnyTitleReturnsDefaultTitle() throws Exception {
	Resource page = $.anAdminPageWithParents("/content/amtliche-mitteilungen/app/detail");
	$.require(anAppService($.aNoteService().build()));
	$.aRequest().parent($.getRequest()).resource(page).params(PARAM_ID, ID_VALUE_NEW).build();
	
	AdminPageController testObj = anAdminPageController(page);
	
	assertEquals(StringUtils.EMPTY, testObj.getView().getTitle());
}

private InjectedService<AppService> anAppService(NoteService noteService) throws Exception {
    InjectedService<AppService> appService = $.service(
            AppService.class, new AppServiceImpl().setNoteService(noteService),
            $.service(ResourceResolverFactory.class, $.getResourceResolverFactory())
    );
    appService.<AppServiceImpl>getInstance().activate($.aComponentContext().build());
    return appService;
}
```
We create an injectable service of `AppService.class` with `$.service(...)`. It is injected into the
test container with `$.require(...).`


### Provide your own adapters
The mechanism for providing own adapters is basically the same as in [Sling](https://sling.apache.org/), 
it is surfaced in the `IAemClient` the the following two methods:
```java
<T1, T2> void registerAdapter(Class<T1> adaptableClass, Class<T2> adapterClass, Function<T1,T2> adaptHandler);
	
<T1, T2> void registerAdapter(Class<T1> adaptableClass, Class<T2> adapterClass, T2 adapter);
```


## General Requirements
AemTesting is build on top of AemMock:

* AEM 6.2
* JDK 1.8 
* Maven >=3.2.5
* Supports AemMock 1.7.0, Sling-Mock 2.0.0


### Installation and configuration
1. Clone the repo.
2. Run ``mvn clean install`` in the project root folder. 
3. Include the artifact as dependency into your AEM project:
```xml
<dependency>
    <groupId>com.quatico.base</groupId>
    <artifactId>aem-testing</artifactId>
    <version>0.2.2</version>
    <scope>test</scope>
</dependency>
```
	Read up on [How to build AEM projects using Maven](https://docs.adobe.com/docs/en/cq/5-6-1/developing/developmenttools/how-to-build-aem-projects-using-apache-maven.html) to get started with Maven.

