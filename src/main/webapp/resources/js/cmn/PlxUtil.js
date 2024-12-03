const PlxUtil = {};

PlxUtil.axiosPost = axios.create({
    method: 'post',    
    header:{
        'Content-Type': 'application/x-www-form-urlencoded'
    },
    transformResponse: [function (data) {        
        return data;
    }],
    transformRequest: [function (data, headers) {            
        return Qs.stringify(data, {arrayFormat: 'brackets'});
    }],
});

PlxUtil.axiosJson = axios.creat({
    method: 'post'
});