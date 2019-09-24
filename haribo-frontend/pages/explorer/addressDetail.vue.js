var exploreraddressDetailView = Vue.component('ExploreraddressDetailView', {
    template:  `
    <div>
        <v-nav></v-nav>
        <v-breadcrumb title="Address Explorer" description="검색한 주소의 결과를 보여줍니다."></v-breadcrumb>
        <div class="container">
        <br><br>
        <div class="row">
            <div class="col-md-12">
            <div class="card shadow-sm">
                <div class="card-header">Address <strong># </strong></div>
                <table class="table">
                    <tbody>
                        <tr>
                            <th width="300">Balance</th>
                            <td></td>
                        </tr>
                        <tr>
                            <th>Texs</th>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            </div>
        </div><br><br>
            <div class="row">
                <div class="col-md-12">
                <div class="card shadow-sm">
                    <div class="card-header"><strong> Transactions </strong></div>
                    <table class="table">
                        <tbody>
                            <tr>
                                <th width="300">트랜잭션 해시</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>블록 넘버</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>날짜</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>송신자 주소</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>전송한 이더</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>Gas</th>
                                <td></td>
                            </tr>
                            <tr>
                                <th>Gas Price</th>
                                <td> bytes</td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                </div>
            </div>
        </div>
    </div>
    `
})
