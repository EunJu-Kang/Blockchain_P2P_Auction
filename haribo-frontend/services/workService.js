var workService = {
    findAll: function(callback){
        $.get(API_BASE_URL + "/api/works", function(data){
            callback(data);
        });
    },
    findWorksByOwner: function(userId, callback){
        $.get(API_BASE_URL + '/api/works/owner/' + userId, function(data){
            callback(data);
        });
    },
    findById: function(workId, callback){
        $.get(API_BASE_URL + "/api/works/" + workId, function(data){
            callback(data);
        });
    },
    findHistoryById: function(workId, callback){
        $.get(API_BASE_URL + "/api/works/history/" + workId, function(data){
            var result = [];

            function loadUser(from, until){
                if(from == until) {
                    callback(result);
                } else {
                    var history = data[from];

                    userService.findById(history.owner, function(user){
                        result.push({
                            createdAt: history.createdAt,
                            owner: user['이름'] + " (" + user['이메일']+ ")"
                        });

                        loadUser(from+1, until);
                    });
                }
            }

            loadUser(0, data.length);
        });
    },
    create: function(body, success, whenError){
        $.ajax({
            type: 'POST',
            url: API_BASE_URL + '/api/works',
            data: JSON.stringify(body),
            headers: { 'Content-Type': 'application/json' },
            success:
              success,
            error:
              whenError
        })
    },
    update: function(body, success, whenError){
      $.ajax({
            type: 'PUT',
            url: API_BASE_URL + '/api/works',
            data: JSON.stringify(body),
            headers: { 'Content-Type': 'application/json' },
            success: success,
            error: whenError
        })
    },
    delete: function(id, success, whenError){
        $.ajax({
            type: 'DELETE',
            url: API_BASE_URL + "/api/works/" + id,
            success: success,
            error: whenError
        });
    }
}
