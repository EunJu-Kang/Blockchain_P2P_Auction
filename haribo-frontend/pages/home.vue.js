var homeView = Vue.component("Home", {
    props: ["isSigned"],
    template: `
        <div>
            <v-nav></v-nav>
            <div id="main-overview" class="container">
                <div class="row">
                    <div class="col-md-6">
                        <h1>DIGITAL CONTENTS<br>AUCTION</h1>
                        <h4>블록체인 기반 미술품 경매를 시작해보세요.</h4>
                    </div>
                    <div class="col-md-6">
                      <img width="100%" height="100%" src="./assets/images/badMan.gif">
                    </div>
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
