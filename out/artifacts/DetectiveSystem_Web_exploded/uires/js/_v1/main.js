require.config({
    baseUrl: "/DetectiveSystem/uires", // 基目录
    shim: {
        "bootstrap": {
            deps: ["jquery"], // 该模块依赖性
            exports: "bootstrap" // 外部调用该模块时的名称
        },
        "DataTables": {
            deps: ["jquery"],
            exports: "DataTables"
        },
        "DataTables_bootstrap": {
            deps: ["jquery", "DataTables"],
            exports: "DataTables_bootstrap"
        },
        "ColReorderWithResize": {
            deps: ["DataTables"],
            exports: "ColReorderWithResize"
        },
        'sco.confirm': {
            deps: ['jquery', 'sco.modal']
        },
        "fileinput": {
            deps: ['jquery', 'bootstrap']
        },
        "fileinput_locale_zh": {
            deps: ['fileinput']
        }
    },
    paths: {
        "jquery": "js/jquery-1.11.2.min",
        "bootstrap": "bootstrap-3.3.4-dist/js/bootstrap.min",
        "DataTables": "DataTables-1.10.7/media/js/jquery.dataTables.min",
        "DataTables_bootstrap": "DataTables-1.10.7/media/js/dataTables.bootstrap",
        "ColReorderWithResize": "DataTables-1.10.7/media/js/ColReorderWithResize",
        'sco.message': 'sco.js-master/js/sco.message',
        'sco.modal': 'sco.js-master/js/sco.modal',
        'sco.confirm': 'sco.js-master/js/sco.confirm',
        'sco.valid': 'sco.js-master/js/sco.valid',
        'sco.tooltip': 'sco.js-master/js/sco.tooltip',

        'fileinput': 'bootstrap-fileinput-master/js/fileinput.min',
        'fileinput_locale_zh': 'bootstrap-fileinput-master/js/fileinput_locale_zh',

        "config": "js/_v1/config",

        "userList": "js/_v1/system/user/userList",
        "suspectsList": "js/_v1/baseInfo/suspects/suspectsList"
    }
});