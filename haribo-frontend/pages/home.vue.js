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
                        <router-link :to="{ name: 'register' }" class="btn btn-lg btn-primary" v-if="!sharedState.isSigned">회원가입</router-link>
                    </div>
                    <div class="col-md-6">
                      <img width="100%" height="100%" src=https://w.namu.la/s/bccb1fc0ec3449aa428c4edd5d4e8392ddf1ed8366c78cd8e122f5e0d6b6c6f14110500554da9aaec8c6e8266ecdf2cb9760b37d9a510f3a0adf205e680fd464819d1b302ad0c062b3bff253101ef4886418da4252d290bc72d64392723fa1d9>
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
