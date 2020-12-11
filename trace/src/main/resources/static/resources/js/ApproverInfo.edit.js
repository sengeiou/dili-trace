class ApproverInfoEdit extends WebConfig {
    constructor(dataForm, submitBtn) {
        super();
        this.dataForm = dataForm;
        this.submitBtn = submitBtn;
        this.submitBtn.on('click', async () => await this.ajaxSubmit());
    }
    async ajaxSubmit() {
        let data = this.dataForm.serializeJSON();
        var data_2 = jq.removeEmptyProperty(data);
        debugger;
        let url = this.toUrl("/approverInfo/insert.action");
        let resp = await jq.postJsonWithProcessing(url, data);
        if (!resp.success) {
            bs4pop.notice(resp.message, { type: 'warning', position: 'topleft' });
            return;
        }
        bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
    }
}
