function postMsg(funName, args = []) {
    let data = { 'fun': funName, 'args': args };
    window.parent.postMessage(JSON.stringify(data));
}
