// import $ from 'jquery';
// import _ from 'underscore';


class NewRegisterBillAdd extends WebConfig {
    form: JQuery;
    submitBtn: JQuery;

    constructor(form: JQuery, submitBtn: JQuery) {
        super();
        this.form = form;
        this.submitBtn=submitBtn;
        this.submitBtn.on('click',async ()=>this.doAdd());
        let categoryController:CategoryController=new CategoryController();
        super.initTraceAutoComplete($("[name='categoryInput']"), function (query, done){ categoryController.lookupCategories(query,done)},function(suggestion){
            $(this).val(suggestion.value);
            $('[name="productId"]').val(suggestion.id);
            $('[name="productName"]').val(suggestion.value);
            //@ts-ignore
            $(this).valid();
        });

        let cityController:CityController=new CityController();
        super.initTraceAutoComplete($("[name='originInput']"), function (query, done){ cityController.lookupCities(query,done)},function(suggestion){
            $(this).val(suggestion.value);
            $('[name="originId"]').val(suggestion.id);
            $('[name="originName"]').val(suggestion.value);
            //@ts-ignore
            $(this).valid();
        });
    }

    public async doAdd() {
        //@ts-ignore
        bs4pop.removeAll();

        let url = super.toUrl("/newRegisterBill/doAdd.action");

        debugger
        //@ts-ignore
        if (!this.form.validate().form()) {
            //@ts-ignore
            bs4pop.notice("请完善必填项", {type: 'warning', position: 'topleft'});
            return;
        }
        let registerBill = super.serializeJSON(this.form);
        try {
            let resp = await jq.postJsonWithProcessing(url, {registerBills:[registerBill]});
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
            //@ts-ignore
            bs4pop.alert('远程访问失败', {type: 'error'});
        }
    }

}
