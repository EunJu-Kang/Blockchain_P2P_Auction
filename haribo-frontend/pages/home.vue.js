var homeView = Vue.component("Home", {
    props: ["isSigned"],
    template: `
        <div>
            <v-nav></v-nav>
            <div id="main-overview" class="container">
                <div class="col" style="text-align:center;">
                  <h2 class="pixelFont">FIGURE AUCTION</h2>
                  <h4>블록체인 기반 피규어 경매를 시작해보세요.</h4>

                  <form class="navbar-form navbar-left row" role="search">
                  <div class="form-group">
                  <input type="text" class="form-control" placeholder="Search">
                  </div>
                  <button type="submit" class="btn btn-default">Submit</button>
                  </form>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            sharedState: store.state
        }
    }
})
