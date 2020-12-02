class popwrapper {
    static async confirm(msg, config = {}) {
        const p = new Promise((resolve, reject) => {
            bs4pop.confirm(msg, config, async function (sure) {
                resolve(sure);
            });
        });
        return await p;
    }
}
//# sourceMappingURL=bs4popWrapper.js.map