// import $ from 'jquery';
// import _ from 'underscore';


class NewRegisterBillAdd extends WebConfig {
    form: JQuery;
    submitBtn: JQuery;

    constructor(form: JQuery, submitBtn: JQuery) {
        super();
        this.form = form;
        this.submitBtn = submitBtn;
        this.submitBtn.on('click', async () => this.doAdd());
        let categoryController: CategoryController = new CategoryController();
        super.initTraceAutoComplete($("[name='categoryInput']"), function (query, done) {
            categoryController.lookupCategories(query, done)
        }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="productId"]').val(suggestion.id);
            $('[name="productName"]').val(suggestion.value);
            //@ts-ignore
            $(this).valid();
        });

        let cityController: CityController = new CityController();
        super.initTraceAutoComplete($("[name='originInput']"), function (query, done) {
            cityController.lookupCities(query, done)
        }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="originId"]').val(suggestion.id);
            $('[name="originName"]').val(suggestion.value);
            //@ts-ignore
            $(this).valid();
        });

        let customerController: CustomerController = new CustomerController();

        super.initTraceAutoComplete($("[name='userInput']"), function (query, done) {
            customerController.lookupSeller(query, done)
        }, function (suggestion) {

            $(this).data('select-text', suggestion.value);
            $(this).val(suggestion.value);
            $('[name="userId"]').val(suggestion.id);
            $('[name="name"]').val(suggestion.item.name);
            //@ts-ignore
            $(this).valid();
        });

        let upstreamController: UpStreamController = new UpStreamController();

        super.initTraceAutoComplete($("[name='upStreamName']"), function (query, done) {
            let userId = $('#userId').val() as number;
            upstreamController.lookupUpStreams(query, userId, done)
        }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="upStreamId"]').val(suggestion.id);
            //@ts-ignore
            $(this).valid();
        });


        let brandController: BrandController = new BrandController();
        super.initTraceAutoComplete($("[name='brandName']"), function (query, done) {
            brandController.lookupBrands(query, done)
        }, function (suggestion) {
            $(this).val(suggestion.value);
            $('[name="upStreamId"]').val(suggestion.id);
            //@ts-ignore
            $(this).valid();
        });

        this.initRegistType();

        //@ts-ignore
        this.form.find('select[multiple]').select2({
            placeholder: '-- 查询选择或输入新增 --'
        });
    }

    private initRegistType() {
        var registerHeadCodeInput = $('input[name="registerHeadCodeInput"]');

        let registerHeadController: RegisterHeadController = new RegisterHeadController();
        super.initTraceAutoComplete(registerHeadCodeInput, function (query, done) {
            $.extend(query, {userId: $('input[name="userId"]').val()})
            registerHeadController.lookupRegisterHead(query, done)
        }, async (suggestion, a, b) => {
            debugger
            $(this).val(suggestion.value);
            let registerHeadCode = suggestion.item.code;

            $('[name="registerHeadCode"]').val(registerHeadCode);

            let plateList = suggestion.item.plateList;
            let arrivalTallynos = suggestion.item.arrivalTallynos;

            debugger

            //@ts-ignore
            $(this).valid();
        });

        $('#registType').on('change', async (e) => {
            if (RegistTypeEnum.PARTIAL != $(e.target).val()) {
                registerHeadCodeInput.parent('div').hide();
                return;
            }
            registerHeadCodeInput.parent('div').show();
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
        let registerBill = super.serializeJSON(this.form, {
            customTypes: {
                hmcustFun: function (strVal, el) {
                    if ($.trim(strVal) != '') {
                        return strVal + ':00';
                    }
                    return strVal;
                }
            }
        });
        try {
            let resp = await jq.postJsonWithProcessing(url, {registerBills: [registerBill]});
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
