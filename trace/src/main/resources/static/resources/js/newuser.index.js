class UserIndex extends ListPage {
    constructor(grid, queryform) {
        super(grid, queryform, queryform.find('#query'), "/ecommerceBill/listPage.action");
        this.grid.on('check.bs.table', async () => await this.rowClick());
        this.grid.on('uncheck.bs.table', async () => await this.rowClick());
        $('#add-btn').on('click', async () => this.openEditPage());
        $('#edit-btn').on('click', async () => this.openEditPage());
        $('#detail-btn').on('click', async () => this.openDetailPage());
    }
    async openDetailPage() {
    }
    async openEditPage() {
    }
    async rowClick() {
        debugger;
    }
}
