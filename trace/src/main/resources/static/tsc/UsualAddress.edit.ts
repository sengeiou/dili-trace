class UsualAddressEdit extends WebConfig {
    dataForm: any;
    submitBtn: any;

    constructor(dataForm: any, submitBtn: any) {
        super();
        this.dataForm = dataForm;
        this.submitBtn = submitBtn;

        let cityController: CityController = new CityController();
        super.initAutoComplete(this.dataForm.find('#addressInput'),
            function (query, done) {
                cityController.lookupCities(query, done)
            }, function (sug) {
                $(this).val(sug.value);
                $('input[name="addressId"]').val(sug.id);
                $('input[name="mergedAddress"]').val(sug.item.mergerName);
                $('input[name="address"]').val(sug.item.name);
            });

        this.submitBtn.on('click',async ()=>await  this.ajaxSubmit());

    }

    private async ajaxSubmit() {
        // if (!this.form.validate().form()) {
        //     //@ts-ignore
        //     bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
        //     return;
        // }

        let data = this.dataForm.serializeJSON();
        var data_2=jq.removeEmptyProperty(data);
        debugger
        let url=this.toUrl("/usualAddress/insert.action");
        let resp =   await jq.postJsonWithProcessing(url, data);
        if(!resp.success){
            //@ts-ignore
            bs4pop.notice(resp.message, {type: 'warning', position: 'topleft'});
            return;
        }
        //@ts-ignore
        bs4pop.alert('操作成功', {type: 'info',autoClose: 600});
    }


}