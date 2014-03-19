/**
 * Views manipulations.
 * Created by Tallevi12
 */
var viewsFactory;
var doT;

viewsFactory = {

    result_row: doT.template('<div class="result_conainer"><div class="result_header">{{=it.head || \"\" }}</div><div class="result_body">{{=it.body || \"\" }}</div></div>')
}

