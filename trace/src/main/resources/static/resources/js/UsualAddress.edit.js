class UsualAddressEdit extends WebConfig {
    constructor(dataForm, submitBtn) {
        super();
        this.dataForm = dataForm;
        this.submitBtn = submitBtn;
        let cityController = new CityController();
        super.initAutoComplete(this.dataForm.find('#addressInput'), function (query, done) {
            cityController.lookupCities(query, done);
        }, function (sug) {
            $(this).val(sug.value);
            $('input[name="addressId"]').val(sug.id);
            $('input[name="mergedAddress"]').val(sug.item.mergerName);
            $('input[name="address"]').val(sug.item.name);
        });
        this.submitBtn.on('click', async () => await this.ajaxSubmit());
    }
    async ajaxSubmit() {
        let data = this.dataForm.serializeJSON();
        var data_2 = jq.removeEmptyProperty(data);
        debugger;
        let url = this.toUrl("/usualAddress/insert.action");
        let resp = await jq.postJsonWithProcessing(url, data);
        if (!resp.success) {
            bs4pop.notice(resp.message, { type: 'warning', position: 'topleft' });
            return;
        }
        bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
    }
}
