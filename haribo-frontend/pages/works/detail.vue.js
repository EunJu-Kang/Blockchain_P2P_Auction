var worksDetailView = Vue.component("WorkDetailView", {
    template: `
        <div>
            <v-nav></v-nav>
            <v-breadcrumb title="작품 이력 보기" description="등록된 작품의 이력을 볼 수 있습니다."></v-breadcrumb>
            <div class="container">
                <div class="row">
                    <div class="col-md-8 mx-auto">
                        <div class="card">
                            <div class="card-body">
                                <div class="form-group">
                                    <label id="user" class="text-secondary">출품자</label>
                                    <p><router-link :to="{ name: 'work.by_user', params: { id: user.id }}">{{ user.name }}({{ user.email }})</router-link></p>
                                </div>
                                <div class="form-group">
                                    <label id="name" class="text-secondary">작품 이름</label>
                                    <p>{{ work.name }}</p>
                                </div>
                                <div class="form-group">
                                    <label id="description" class="text-secondary">작품 설명</label>
                                    <p>{{ work.description }}</p>
                                </div>
                                <div class="form-group">
                                    <label id="isActive" class="text-secondary">공개여부</label><br>
                                    <p>{{ work.isActive }}</p>
                                </div>
                                <div class="form-group">
                                    <label id="status" class="text-secondary">상태</label><br>
                                    <p>{{ work.status }}</p>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 mb-5">
                                        <table class="table table-striped table-bordered">
                                            <thead>
                                                <tr v-if="history.length == 0">
                                                    <th>등록된 이력이 없습니다.</th>
                                                </tr>
                                                <tr v-if="history.length > 0">
                                                    <th width="30%">소유일</th>
                                                    <th width="30%">작품명</th>
                                                    <th width="40%">소유자</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr v-for="item in history">
                                                    <td>{{ item.createdAt }}</td>
                                                    <td>{{ work.name }}</td>
                                                    <td>{{ item.owner }}</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <button v-on:click="goBack" class="btn btn-sm btn-outline-secondary">뒤로가기</button>
                                    </div>
                                    <div class="col-md-6 text-right" v-if="sharedStates.user.id == work.ownerId">
                                        <router-link :to="{ name: 'work.update', params: { id:work.id } }" class="btn btn-sm btn-primary">작품 수정하기</router-link>
                                        <button class="btn btn-sm btn-danger" v-on:click="deleteWork">작품 삭제하기</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `,
    data(){
        return {
            work: {
                id: 0,
                name: "",
                description: "",
                isActive: "",
                status: "",
                ownerId: 0
            },
            user: {
                id: 0,
                name: "",
                email: ""
            },
            history: [],
            sharedStates: store.state
        }
    },
    methods: {
        goBack: function(){
            // 이전 페이지로 이동한다.
            this.$router.go(-1);
        },
        deleteWork: function(){
            var scope = this;
            workService.delete(
                this.$route.params.id, 
                function(response){
                    alert("작품이 삭제되었습니다.");
                    scope.$router.push('/artworks');
                },
                function(error){
                    alert("작품을 삭제할 수 없습니다.");
                }
            );
        }
    },
    mounted: function(){
        var scope = this;
        var workId = this.$route.params.id;

        // 작품 상세 정보 조회
        workService.findById(workId, function(data){
            scope.work.id = workId;
            scope.work.name = data["이름"];
            scope.work.description = data["설명"];
            scope.work.isActive = data["공개여부"];
            scope.work.status = data["상태"];
            scope.work.ownerId = data["회원id"];

            userService.findById(scope.work.ownerId, function(user){
                scope.user.id = user["id"];
                scope.user.name = user["이름"];
                scope.user.email = user["이메일"];
            });
        });

        // 작품 이력 조회
        workService.findHistoryById(workId, function(data){
            scope.history = data;
        });
    }
})