package com.bpm.framework.easy.tag;

import javax.servlet.jsp.JspException;

public class TableSelectorTag extends EasyTag {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8870771753000419718L;

	@Override
	public int doEndTag() throws JspException {
		// DefaultService<?> defaultService = (DefaultService<?>) SpringContext.getBean("defaultService");
		// List<?> list = defaultService.findBySql(SqlConfigXmlHandler.getInstance().getSqlBean(sqlKey).getValue(), null);
//		BootstrapDialog.show({
//  		  title: '<spring:message code="pic.common.addDescription"></spring:message>',
//  		  draggable: true,  // 允许拖曳
//  		  type: BootstrapDialog.TYPE_INFO,  //决定title的颜色
//  		  message: $('<div></div>').load('../common/addTl.jsp'),
//  		  buttons: [{
//                label: '<spring:message code="fw.common.action.ok" ></spring:message>',
//                cssClass: 'btn-primary',
//                action: function(dialogItself){
//              	 var language = dialogItself.getModalBody().find('#tlLanguage').val();
//               	 var text = dialogItself.getModalBody().find('#inputText').val();
//               	 //删除已有的
//                 	 $("div[language="+language+"]").remove();
//               	 
//               	 $("#positionTlDiv").append('<div class="alert alert-dismissible fade in alert-info col-xs-4" role="alert" name="tl" language='+language+' text='+text+'><button type="button" class="close" data-dismiss="alert" aria-label="Close"> <span aria-hidden="true">x</span> </button> '+language+":"+text+'</div>')
//                  
//               	 dialogItself.close();
//                }
//            },  {
//                label: '<spring:message code="fw.common.action.close" ></spring:message>',
//                action: function(dialogItself){
//              	
//                    dialogItself.close();
//                }
//            }]
//        });
		StringBuilder html = new StringBuilder();
		html.append("<input id=\"").append(super.getId())
			.append("\" name=\"").append(super.getName())
			.append("\" class=\"easyui-textbox\"").append(" readonly ").append(" />");
		html.append("<img class=\"popImg\" src=\"../resources/images/search.gif\" />");
		this.write(html);
		return EVAL_PAGE;
	}
}
