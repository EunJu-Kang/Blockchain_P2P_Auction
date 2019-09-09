var homeView = Vue.component("Home", {
    template: `
        <div>
            <v-nav></v-nav>
            <div id="main-overview" class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h1>DIGITAL CONTENTS<br>AUCTION</h1>
                        <h4>블록체인 기반 미술품 경매를 시작해보세요.</h4>
                        <router-link :to="{ name: 'register' }" class="btn btn-lg btn-primary">회원가입</router-link>
                    </div>
                </div>
            </div>
        </div>
    `
})