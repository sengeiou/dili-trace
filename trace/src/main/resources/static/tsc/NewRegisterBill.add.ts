// import $ from 'jquery';
// import _ from 'underscore';


class NewRegisterBillAdd extends WebConfig {
    form: JQuery;
    submitBtn: JQuery;

    constructor(form: JQuery, submitBtn: JQuery) {
        super();
        this.form = form;
        this.submitBtn.on('click',this.doAdd)
    }

    public async doAdd() {
        //@ts-ignore
        bs4pop.removeAll();

        let url = this.toUrl("/newRegisterBill/doAdd.action");
        var data = (this.submitBtn as any).serializeJSON({useIntKeysAsArrayIndex: true});
        try {
            var resp = await jq.postJsonWithProcessing(url, data);
            if (!resp.success) {
                //@ts-ignore
                bs4pop.alert(resp.message, {type: 'error'});
                return;
            }
            //@ts-ignore
            bs4pop.removeAll()
            //@ts-ignore
            bs4pop.alert('操作成功', {type: 'info', autoClose: 600});
        } catch (e) {
            debugger
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

}
