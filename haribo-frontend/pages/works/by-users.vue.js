/**
 * 화면명 : 사용자 별 작품 조회
 */

var worksByUserView = Vue.component('WorksByUserView', {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="사용자 별 작품 조회" description="특정 사용자가 등록한 작품의 목록을 보여줍니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-8 mx-auto">
                        <div class="card">
                            <div class="card-header">'{{ user.name }}({{ user.email }})'님의 작품 목록</div>
                            <div class="card-body">
                                <div v-for="work in works">
                                    <router-link :to="{ name: 'work.detail', params: { id: work.id } }" class="work-list-item">
                                        <h4>{{ work.title }}</h4>
                                        <p>{{ work.description }}</p>
                                    </router-link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data() {
        return {
            user: {
                id: 0,
                name: "",
                email: ""
            },
            works: [{
                id: 0,
                title: "",
                description: ""
            }]
        }
    },
    mounted: function(){
        var scope = this;
        var userId = this.$route.params.id;

        workService.findWorksByOwner(userId, function(data){
            var result = data;
            scope.works = [];

            for(var i = 0; i < result.length; i++) {
                scope.works.push({
                    id: result[i]['id'],
                    title: result[i]['이름'],
                    description: result[i]['설명'] != "" ? "-" : result[i]['설명']
                });
            }
        });

        userService.findById(userId, function(data){
            scope.user.id = data["id"];
            scope.user.name = data["이름"];
            scope.user.email = data["이메일"];
        });
    }
})