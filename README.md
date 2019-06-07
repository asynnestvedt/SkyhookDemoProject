# SkyhookDemoProject

* libSkyhook module builds a jar in /out/... folder
* src/Main pulls in this jar. this likely needs to be reconfigured per environment
* Main is a client that spawns multiple threads to demo the library. library has threadsafe shared cache.
* Models are not threadsafe since they're not being accessed by multiple threads in this demo
* *read code TODO stubs for more info*
* noteworthy omissions include lack of tests, incomplete error handling, incomplete documentation, lack of linting, overly broad scopes on class attributes.
