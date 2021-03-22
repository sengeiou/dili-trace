class NewRegisterBillAdd extends WebConfig {
    constructor(form, submitBtn) {
        super();
        this.form = form;
        this.submitBtn = submitBtn;
        this.submitBtn.on('click', async () => this.doAdd());
        //初始化经营户自动补全框
        let customerController = new CustomerController();
        super.initTraceAutoComplete($("[name='userInput']"), function (query, done) { customerController.lookupSeller(query, done); }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="userId"]').val(suggestion.id);
            $('[name="name"]').val(suggestion.item.name);
            $(this).valid();
        });
        let categoryController = new CategoryController();
        super.initTraceAutoComplete($("[name='categoryInput']"), function (query, done) { categoryController.lookupCategories(query, done); }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="productId"]').val(suggestion.id);
            $('[name="productName"]').val(suggestion.value);
            $(this).valid();
        });
        let cityController = new CityController();
        super.initTraceAutoComplete($("[name='originInput']"), function (query, done) { cityController.lookupCities(query, done); }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="originId"]').val(suggestion.id);
            $('[name="originName"]').val(suggestion.value);
            $(this).valid();
        });
    }
    async doAdd() {
        bs4pop.removeAll();
        let url = super.toUrl("/newRegisterBill/doAdd.action");
        debugger;
        if (!this.form.validate().form()) {
            bs4pop.notice("请完善必填项", { type: 'warning', position: 'topleft' });
            return;
        }
        let registerBill = super.serializeJSON(this.form);
        try {
            let resp = await jq.postJsonWithProcessing(url, { registerBills: [registerBill] });
            if (!resp.success) {
                bs4pop.alert(resp.message, { type: 'error' });
                return;
            }
            bs4pop.removeAll();
            bs4pop.alert('操作成功', { type: 'info', autoClose: 600 });
        }
        catch (e) {
            bs4pop.alert('远程访问失败', { type: 'error' });
        }
    }
}
