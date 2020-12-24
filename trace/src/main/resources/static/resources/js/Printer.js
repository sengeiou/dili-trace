class Printer {
    constructor(templateName) {
        this.templateName = templateName;
    }
    async boothPrintPreview(data, previewFlag) {
        let printerPoint = callbackObj;
        if (printerPoint && typeof (printerPoint) != 'undefined' && printerPoint.printDirect) {
            $('.btn-main2').hide();
            printerPoint.boothPrintPreview(JSON.stringify(data), this.templateName, previewFlag);
        }
    }
}
