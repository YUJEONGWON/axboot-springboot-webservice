var fnObj = {};
var ACTIONS = axboot.actionExtend(fnObj, {
    PAGE_SEARCH: function (caller, act, data) {
        var paramObj = $.extend(caller.searchView.getData(), data);

        axboot.ajax({
            type: "GET",
            url: "/api/v1/education/yjGridForm/pages",
            data: paramObj,
            callback: function (res) {
                caller.formView01.clear();
                caller.gridView01.setData(res);
            },
            options: {
                // axboot.ajax 함수에 2번째 인자는 필수가 아닙니다. ajax의 옵션을 전달하고자 할때 사용합니다.
                onError: function (err) {
                    console.log(err);
                },
            },
        });
    },
    PAGE_SAVE: function (caller, act, data) {
        if (caller.formView01.validate()) {
            var item = caller.formView01.getData();
            console.log(item);
            if (!item.id) item.__created__ = true;
            axboot.ajax({
                type: "POST",
                url: "/api/v1/education/yjGridForm",
                data: JSON.stringify(item),
                callback: function (res) {
                    axToast.push("저장 되었습니다");
                    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                },
            });
        }

        //     var saveList = [].concat(caller.gridView01.getData("modified"));
        //     saveList = saveList.concat(caller.gridView01.getData("deleted"));
    },
    PAGE_DELETE: function (caller, act, data) {
        axDialog.confirm({ msg: "삭제하시겠습니까?" }, function () {
            if (this.key == "ok") {
                var items = caller.gridView01.getData("selected");
                if (!items.length) {
                    axDialog.alert("삭제할 데이터가 없습니다.");
                    return false;
                }
                var ids = items.map(function (value) {
                    return value.id;
                });

                axboot.ajax({
                    type: "DELETE",
                    url: "/api/v1/education/yjGridForm?ids=" + ids.join(","),
                    callback: function (res) {
                        axToast.push("삭제 되었습니다");
                        ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
                    },
                });
            }
        });
    },
    //클릭하면 id로 데이터 찾아옴
    ITEM_CLICK: function (caller, act, data) {
        var id = (data || {}).id;
        if (!id) {
            axDialog.alert("id는 필수입니다");
            return false;
        }
        axboot.ajax({
            type: "GET",
            url: "/api/v1/education/yjGridForm/" + id,
            callback: function (res) {
                caller.formView01.setData(res);
            },
        });
    },
    FORM_CLEAR: function (caller, act, data) {
        axDialog.confirm({ msg: LANG("ax.script.form.clearconfirm") }, function () {
            if (this.key == "ok") {
                caller.formView01.clear();
                $('[data-ax-path="companyNm"]').focus();
            }
        });
    },

    dispatch: function (caller, act, data) {
        var result = ACTIONS.exec(caller, act, data);
        if (result != "error") {
            return result;
        } else {
            // 직접코딩
            return false;
        }
    },
    // ITEM_ADD: function (caller, act, data) {
    //     caller.gridView01.addRow();
    // },
    // ITEM_DEL: function (caller, act, data) {
    //     caller.gridView01.delRow("selected");
    // },
});

// fnObj 기본 함수 스타트와 리사이즈
fnObj.pageStart = function () {
    this.pageButtonView.initView();
    this.searchView.initView();
    this.gridView01.initView();
    this.formView01.initView();

    ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
};

fnObj.pageResize = function () {};

fnObj.pageButtonView = axboot.viewExtend({
    initView: function () {
        axboot.buttonClick(this, "data-page-btn", {
            search: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            },
            save: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_SAVE);
            },
            excel: function () {},
            fn1: function () {
                ACTIONS.dispatch(ACTIONS.PAGE_DELETE);
            },
        });
    },
});

//== view 시작
/**
 * searchView
 */
fnObj.searchView = axboot.viewExtend(axboot.searchView, {
    initView: function () {
        this.target = $(document["searchView0"]);
        this.target.attr("onsubmit", "return false;");
        this.target.on("keydown.search", "input,.form-control", function (e) {
            if (e.keyCode === 13) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
            }
        });

        this.useYn = $(".js-useYn").on("change", function () {
            ACTIONS.dispatch(ACTIONS.PAGE_SEARCH);
        });
        this.filter = $(".js-filter");
    },
    getData: function () {
        return {
            pageNumber: this.pageNumber || 0,
            pageSize: this.pageSize || 50,
            useYn: this.useYn.val(),
            filter: this.filter.val(),
        };
    },
});

/**
 * gridView
 */
fnObj.gridView01 = axboot.viewExtend(axboot.gridView, {
    initView: function () {
        this.target = axboot.gridBuilder({
            onPageChange: function (pageNumber) {
                ACTIONS.dispatch(ACTIONS.PAGE_SEARCH, { pageNumber: pageNumber });
            },
            showRowSelector: true,
            frozenColumnIndex: 0,
            multipleSelect: true,
            target: $('[data-ax5grid="grid-view-01"]'),
            columns: [
                { key: "companyNm", label: COL("company.name"), width: 120, align: "left" },
                { key: "ceo", label: COL("company.ceo"), width: 70, align: "center" },
                { key: "useYn", label: COL("use.or.not"), align: "center" },
                { key: "bizno", label: COL("company.bizno"), width: 100, formatter: "bizno", align: "center" },
            ],
            body: {
                onClick: function () {
                    this.self.select(this.dindex, { selectedClear: true });
                    ACTIONS.dispatch(ACTIONS.ITEM_CLICK, this.item);
                },
            },
        });

        // axboot.buttonClick(this, "data-grid-view-01-btn", {
        //     add: function () {
        //         ACTIONS.dispatch(ACTIONS.ITEM_ADD);
        //     },
        //     delete: function () {
        //         ACTIONS.dispatch(ACTIONS.ITEM_DEL);
        //     },
        // });
    },
    // getData: function (_type) {
    //     var list = [];
    //     var _list = this.target.getList(_type);

    //     if (_type == "modified" || _type == "deleted") {
    //         list = ax5.util.filter(_list, function () {
    //             delete this.deleted;
    //             return this.key;
    //         });
    //     } else {
    //         list = _list;
    //     }
    //     return list;
    // },
    // addRow: function () {
    //     this.target.addRow({ __created__: true }, "last");
    // },
});

fnObj.formView01 = axboot.viewExtend(axboot.formView, {
    initView: function () {
        var _this = this; // fnObj.formView01

        _this.target = $(".js-form");

        this.model = new ax5.ui.binder();
        this.model.setModel(this.getDefaultData(), this.target);
        this.modelFormatter = new axboot.modelFormatter(this.model); // 모델 포메터 시작

        this.initEvent();
    },
    getDefaultData: function () {
        return { useYn: "Y" };
    },
    getData: function () {
        var data = this.modelFormatter.getClearData(this.model.get());
        data = $.extend({}, data);
        return data;
    },
    setData: function (data) {
        if (typeof data === "undefined") data = this.getDefaultData();
        data = $.extend({}, data);

        this.model.setModel(data);
        this.modelFormatter.formatting();
    },
    validate: function () {
        var item = this.model.get();

        var rs = this.model.validate(); //binder의 함수
        if (rs.error) {
            axDialog.alert(LANG("ax.script.form.validate", rs.error[0].jquery.attr("title")), function () {
                rs.error[0].jquery.focus();
            });
            return false;
        }

        var pattern;
        if (item.email) {
            pattern = /^[A-Za-z0-9]([-_.]?[A-Za-z0-9])*@[A-Za-z0-9]([-_.]?[A-Za-z0-9])*\.(?:[A-Za-z0-9]{2,}?)$/i;
            if (!pattern.test(item.email)) {
                axDialog.alert("이메일 형식을 확인하세요.", function () {
                    $('[data-ax-path="email"]').focus();
                });
                return false;
            }
        }

        if (item.bizno && !(pattern = /^([0-9]{3})\-?([0-9]{2})\-?([0-9]{5})$/).test(item.bizno)) {
            axDialog.alert("사업자번호 형식을 확인하세요."),
                function () {
                    $('[data-ax-path="bizno"]').focus();
                };
            return false;
        }

        return true;
    },
    initEvent: function () {
        axboot.buttonClick(this, "data-form-view-01-btn", {
            "form-clear": function () {
                ACTIONS.dispatch(ACTIONS.FORM_CLEAR);
            },
        });
    },
});
