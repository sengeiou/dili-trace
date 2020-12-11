class ApproverInfoEdit extends WebConfig {
    constructor(dataForm, submitBtn) {
        super();
        this.dataForm = dataForm;
        this.submitBtn = submitBtn;
        this.pw = window.parent.window;
        this.submitBtn.on('click', async () => await this.ajaxSubmit());
    }
    async ajaxSubmit() {
        if (!this.dataForm.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        let data = this.dataForm.serializeJSON();
        var data_2 = jq.removeEmptyProperty(data);
        debugger;
        var url = null;
        if (data.id == null || data.id == "") {
            url = "/approverInfo/insert.action";
        }
        else {
            url = "/approverInfo/update.action";
        }
        let resp = await jq.postJsonWithProcessing(url, data);
        if (!resp.success) {
            bs4pop.notice(resp.message, { type: 'warning', position: 'topleft' });
            return;
        }
        p.call("editSuccess");
    }
}
