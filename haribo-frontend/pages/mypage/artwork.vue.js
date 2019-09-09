var myArtworkView = Vue.component('MyArtworkView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="마이페이지" description="지갑을 생성하거나 작품을 업로드 할 수 있습니다."></v-breadcrumb>
            <div class="container">
                <v-mypage-nav></v-mypage-nav>
                <div class="row">
                    <div class="col-md-12 text-right">
                        <router-link to="/works/create" class="btn btn-outline-secondary">내 작품 등록하기</router-link>
                    </div>
                </div>
                <div id="my-artwork" class="row">
                    <div class="col-md-12 mt-5">
                        <h4>보유 중</h4>
                        <div class="row">
                            <div class="col-md-3 artwork" v-for="item in artworks" v-if="artworks.length > 0">
                                <div class="card">
                                    <div class="card-body">
                                        <img src="./assets/images/artworks/artwork1.jpg">
                                        <h4>{{ item["이름"] }}</h4>
                                        <p v-if="item['설명'] != null">{{ item["설명"] }}</p>
                                        <p v-if="item['설명'] == null">-</p>
                                        <router-link :to="{ name: 'work.detail', params: { id: item['id'] } }" class="btn btn-block btn-secondary">자세히보기</router-link>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-8 mt-3" v-if="artworks.length == 0">
                                <div class="alert alert-warning">보유중인 작품이 없습니다.</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 mt-5">
                        <h4>경매 중</h4>
                        <div class="row">
                            <div class="col-md-3 artwork" v-for="item in auctions" v-if="auctions.length > 0">
                                <div class="card">
                                    <div class="card-body">
                                        <img src="./assets/images/artworks/artwork1.jpg">
                                        <h4>{{ item['작품정보']['이름'] }}</h4>
                                        <span class="badge badge-success">경매 진행중</span>
                                        <router-link :to="{ name: 'auction.detail', params: { id: item['id'] }}" class="btn btn-block btn-secondary mt-3">자세히보기</router-link>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-12 col-md-8 mt-3" v-if="auctions.length == 0">
                                <div class="alert alert-warning">진행중인 경매 목록이 없습니다.</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            sharedStates: store.state,
            artworks: [],
            auctions: []
        }
    },
    methods: {
        calculateDate(date) {
            var now = new Date();
            var endDate = new Date(date);
            var diff = endDate.getTime() - now.getTime();

            // 만약 종료일자가 지났다면 "경매 마감"을 표시한다.
            if(diff < 0) {
                return "경매 마감";
            } else {
                // UNIX Timestamp를 자바스크립트 Date객체로 변환한다.
                var d = new Date(diff);
                var days = d.getDate();
                var hours = d.getHours();
                var minutes = d.getMinutes();

                return "남은시간: " + days + "일 " + hours + "시간 " + minutes + "분";
            }
        }
    },
    mounted: function(){
        var scope = this;
        var userId = this.sharedStates.user.id;

        /**
         * TODO 1. 회원의 작품 목록을 가져옵니다.
         * Backend와 API 연동합니다.
         * 작품 마다 소유권 이력을 보여줄 수 있어야 합니다.
         */
         // 여기에 작성하세요.

        /**
         * TODO 2. 회원의 경매 목록을 가져옵니다.
         * Backend와 API 연동합니다.
         * 경매 중인 작품 마다 소유권 이력을 보여줄 수 있어야 합니다.
         */
         // 여기에 작성하세요.

    }
})
