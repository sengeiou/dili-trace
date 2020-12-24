class Printer{
    private templateName:string;
    constructor(templateName:string) {
        this.templateName=templateName;
    }
    public async boothPrintPreview(data:object,previewFlag:number){
        //@ts-ignore
        let printerPoint=callbackObj;

        if(printerPoint&&typeof(printerPoint)!='undefined'&&printerPoint.printDirect){
            $('.btn-main2').hide();
            printerPoint.boothPrintPreview(JSON.stringify(data),this.templateName,previewFlag);
        }
    }
}