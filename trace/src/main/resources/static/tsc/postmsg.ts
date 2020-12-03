function postMsg(funName:string,args:any[]=[]){
    let data={'fun':funName,'args':args};
    // @ts-ignore
    window.parent.postMessage(JSON.stringify(data));
}

