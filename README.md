现在需要把杭州和寿光的整合到一个项目<br/>
时间关系，一些页面我们不做整合和调整，比如列表页或者报备单创建页面等，直接加后缀，然后在代码里面做判断。
然后需要接入流程引擎，这个引擎接口已经被封装过一次，不确定是否好用。基本的几个入口封装都在包com.dili.bpmc.sdk.rpc里面<br/>

我现在添加了webctxservice，这个里面用来获取当前登录用户的信息，不直接从sessioncontext获得，后面在测试的时候，偶尔会很麻烦<br/>

还有，对于各种rpc接口，我建议在service包里面再做一次简单的封装，FirmRpc封装为FirmRpcService，主要是
考虑到对一些返回结果的处理.(比如对Baseoutput的处理，或者是对单个返回数据结果处理为Optional等)<br/>

其次我也引入了typescript，你们安装好nodejs，引入 环境变量,在trace目录下直接npm install,之后在tsconfig.json文件所在目录执行命令: tsc -w
就会自动编译ts了。ts以及js文件的位置在tsconfig.json文件里面有配置，可以直接查看。<br/>

不确定你们对ts了解不，断点之类 的可以直接在ts里面打，然后刷新页面就好，不用debugger或者去浏览器打断点。有时候文件比较多，不好找代码行.
其次ts的语法更像java，如果之前不熟悉，很快也能简单了解一下。

目前项目并没有接入nacos等，我们还是先用不同的profile来处理，到最后再去迁移，不然依赖的环境太多，经常出各种问题。
