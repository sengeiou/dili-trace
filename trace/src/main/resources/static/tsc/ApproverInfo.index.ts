class ApproverInfoIndex extends  ListPage{
    constructor(grid: any, queryform:any) {
        super(grid,queryform,queryform.find('#query'),"/approverInfo/listPage.action");

        var cthis=this;
        window['ApproverInfoGridObj']=this;

        $('#add-btn').on('click',async ()=>this.openAddPage());
        $('#edit-btn').on('click',async ()=>this.openEditPage());
        $('#detail-btn').on('click',async ()=>this.openDetailPage());

        window.addEventListener('message', function(e) {
            var data=JSON.parse(e.data);
            if(data.obj&&data.fun){
                if(data.obj=='ApproverInfoGridObj'){
                    cthis[data.fun].call(cthis,data.args)
                }
            }

        }, false);
    }

    private async  openAddPage(){
        let url = this.toUrl("/approverInfo/edit.html");

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '增加签名',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '80%',
            btns: []
        });
    }

    private async  openEditPage(){
        if(this.rows.length==0){
            //@ts-ignore
            bs4pop.alert('请选择要修改的数据', {type: 'error'});
            return;
        }
        var select=this.rows[0];
        let url = this.toUrl("/approverInfo/edit.html?id="+select.id);

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '修改签名',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '80%',
            btns: []
        });
    }

    private async openDetailPage(){
        if(this.rows.length==0){
            //@ts-ignore
            bs4pop.alert('请选择要查看的数据', {type: 'error'});
            return;
        }
        var select=this.rows[0];
        let url = this.toUrl("/approverInfo/view.html?id="+select.id);

        //@ts-ignore
        var dia = bs4pop.dialog({
            title: '查看签名详情',
            content: url,
            isIframe: true,
            closeBtn: true,
            backdrop: 'static',
            width: '40%',
            height: '80%',
            btns: []
        });
    }
}