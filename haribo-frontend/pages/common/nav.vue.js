var navVue = Vue.component("v-nav", {
    props: ["isSigned"],
    template: `
        <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-primary">
            <div class="container">
                <router-link class="navbar-brand" to="/">Auction | HARIBO</router-link>
                <div class="navbar-collapse offcanvas-collapse" id="navbarsExampleDefault">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item">
                            <router-link class="nav-link" to="/artworks">Artworks</router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/auction">경매참여하기</router-link>
                        </li>
                        <li class="nav-item">
                            <router-link class="nav-link" to="/explorer/auctions">익스플로러</router-link>
                        </li>
                        <li class="nav-item" v-if="sharedState.isSigned">
                            <router-link class="nav-link" to="/mypage/wallet_create" v-if="!sharedState.user.hasWallet">마이페이지</router-link>
                            <router-link class="nav-link" to="/mypage/wallet_info" v-if="sharedState.user.hasWallet">마이페이지</router-link>
                        </li>
                        <li class="nav-item" v-if="!sharedState.isSigned">
                            <router-link class="nav-link" to="/login">로그인</router-link>
                        </li>
                        <li class="nav-item" v-if="!sharedState.isSigned">
                            <router-link class="nav-link" to="/register">회원가입</router-link>
                        </li>
                        <li class="nav-item" v-if="sharedState.isSigned">
                            <router-link class="nav-link" to="/logout">로그아웃</router-link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    `,
    data() {
        return {
            sharedState: store.state
        }
    }
})