package kuorum

import kuorum.core.FileGroup
import kuorum.law.Law
import kuorum.web.commands.customRegister.Step2Command
import org.bson.types.ObjectId
import org.codehaus.groovy.grails.validation.*

class FormTagLib {
    static defaultEncodeAs = 'raw'
    //static encodeAsForTags = [tagName: 'html']

    def grailsApplication

    static namespace = "formUtil"

    def editImage ={attrs ->
        def command = attrs.command
        def field = attrs.field
        def kuorumImageId = command."$field"
        KuorumFile kuorumFile = null
        FileGroup fileGroup = null
        def value = ""
        def imageUrl = ""
        if (kuorumImageId)
            kuorumFile = KuorumFile.get(new ObjectId(kuorumImageId))

        if (!kuorumFile){
            kuorumImageId = "__NEW__"
            fileGroup = attrs.fileGroup
        }else{
            value = kuorumImageId
            fileGroup =kuorumFile.fileGroup
            imageUrl = kuorumFile.url
        }
        def model = [
                imageId: kuorumImageId,
                value:value,
                fileGroup:fileGroup,
                imageUrl:imageUrl,
                name:field
        ]
        out << g.render(template:'/layouts/form/uploadImage', model:model)
    }

    private static final Integer NUM_CHARS_SHORTEN_URL = 18 //OWLY
    def postTitleLimitChars = {attrs->
        Law law = attrs.law
        out << grailsApplication.config.kuorum.post.titleSize - law.hashtag.size() - NUM_CHARS_SHORTEN_URL

    }

    def input={attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def helpBlock = attrs.helpBlock?:''
        def type = attrs.type?:'text'
        def required = attrs.required?'required':''
        def cssClass = attrs.cssClass?:'form-control input-lg'
        def labelCssClass = attrs.labelCssClass?:''
        def maxlength = attrs.maxlength?"maxlength='${attrs.maxlength}'":''

        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = message(code: "${command.class.name}.${field}.placeHolder", default: '')

        def value = command."${field}"?:''
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}" class="${labelCssClass}">${label}</label>
            <input type="${type}" name="${field}" class="${cssClass} ${error}" id="${id}" ${required} ${maxlength} placeholder="${placeHolder}" value="${value}">
        """
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (helpBlock){
            out << "<p class='help-block'>${helpBlock}</p>"
        }
    }

    def selectEnum = {attrs->
        def command = attrs.command
        def field = attrs.field

        def id = attrs.id?:field
        def cssClass = attrs.cssClass
        def clazz = command.metaClass.properties.find{it.name == field}.type
        def label = message(code: "${clazz.name}.label")
        def error = hasErrors(bean: command, field: field,'error')
        out <<"""
            <label for="${id}">${label}</label>
            <select name="${field}" class="form-control ${error}" id="${id}">
            """
        out << "<option value=''> ${message(code:"${clazz.name}.empty")}</option>"
        clazz.values().each{
            String codeMessage = "${clazz.name}.$it"
            out << "<option value='${it}' ${it==command."$field"?'selected':''}> ${message(code:codeMessage)}</option>"
        }
        out << "</select>"
        if(error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: name)}</span>"
        }
    }

    def textArea = {attrs ->
        Step2Command command = attrs.command
        def field = attrs.field
        def rows = attrs.rows?:5

        def id = attrs.id?:field
        def value = command."$field"?:''
//        def cssClass = attrs.cssClass
        def label = message(code: "${command.class.name}.${field}.label")
        def placeHolder = message(code: "${command.class.name}.${field}.placeHolder")
        def error = hasErrors(bean: command, field: field,'error')
        ConstrainedProperty constraints = command.constraints.find{it.key.toString() == field}.value
        MaxSizeConstraint maxSizeConstraint = constraints.appliedConstraints.find{it instanceof MaxSizeConstraint}
        def maxSize = maxSizeConstraint.maxSize?:0


        out << """
            <label for="${id}">${label}</label>
            <textarea name='${field}' class="form-control counted ${error}" rows="${rows}" id="${id}" placeholder="${placeHolder}">${value}</textarea>
        """
        if (error){
            out << "<span for='${id}' class='error'>${g.fieldError(bean: command, field: field)}</span>"
        }

        if (maxSize){
        out << """
            <div id="charInit" class="hidden">${message(code:'form.textarea.limitChar')}<span>${maxSize}</span></div>
            <div id="charNum" class="help-block">${message(code:'form.textarea.limitChar.left')} <span></span> ${message(code:'form.textarea.limitChar.characters')}</div>
            """
        }
    }

    /* VALIDATION */

    private def getValue(def command, String field){
        def res = command
        field.split("\\.").each {res = res."$it"}
        res
    }

    private void printValidationType (restrictions, messages,constraint,filedName){
        String prefixMessage = constraint.constraintOwningClass.name
        def clazz = constraint.constraintOwningClass

        if (clazz.getDeclaredFields().find{it.name==filedName}?.type ==Integer.class){
            restrictions.append("number: true ,")
            String code = prefixMessage + ".${filedName}.notNumber"
            String text = g.message(code:code)
            messages.append("number: '${text}',")
        }
    }
    private void printValidation (restrictions, messages,constraint,filedName){
        String prefixMessage = constraint.constraintOwningClass.name

        if (constraint instanceof NullableConstraint){
            restrictions.append("required: ${!constraint.nullable} ,")
//            restrictions.append("blank: ${constraint.nullable},")
            String code = prefixMessage + ".${filedName}.nullable"
            String text = g.message(code:code)
            messages.append("required: '${text}',")
//            messages.append("blank: '${text}',")
//        }else if (constraint instanceof BlankConstraint){
//            restrictions.append("blank: ${constraint.blank},")
//            String code = prefixMessage + ".${filedName}.blank"
//            String text = g.message(code:code)
//            messages.append("blank: '${text}',")
        }else if (constraint instanceof MinConstraint && !(constraint.minValue instanceof Date)){
            restrictions.append("min: ${constraint.minValue},")
            String code = prefixMessage + ".${filedName}.min"
            String text = g.message(code:code,args:[constraint.minValue])
            messages.append("min: '${text}',")
        }else if (constraint instanceof MinSizeConstraint){
            restrictions.append("minlength: ${constraint.minSize},")
            String code = prefixMessage + ".${filedName}.min.size"
            String text = g.message(code:code,args:[constraint.minSize])
            messages.append("minlength: '${text}',")
        }else if (constraint instanceof MatchesConstraint){
            restrictions.append("regex: /${constraint.regex}/,")
            String code = prefixMessage + ".${filedName}.matches"
            String text = g.message(code:code)
            messages.append("regex: '${text}',")
        }else if (constraint instanceof MaxSizeConstraint){
            restrictions.append("maxlength: ${constraint.maxSize},")
            String code = prefixMessage + ".${filedName}.max.size"
            String text = g.message(code:code,args:[constraint.maxSize])
            messages.append("maxlength: '${text}',")
        }else if (constraint instanceof EmailConstraint){
            restrictions.append("email: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("email: '${text}',")
        }else if (constraint instanceof UrlConstraint && constraint.url){
            restrictions.append("url: true,")
            String code = prefixMessage + ".${filedName}.wrongFormat"
            String text = g.message(code:code)
            messages.append("url: '${text}',")
        }
    }


    private String tranlateErrorCode(def codes){
        String msg = ""
        codes.each {code ->
            if (!msg) msg = g.message(code: code, default: "")
        }
        if (!msg) msg = g.message(code: "${codes[codes.size()-1]}")
        msg
    }
    private void printGeneralErrors(def errors, def bean){
        if (errors){
            out << """
            <script>
                \$(document).ready(function (){
                    """
            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                out << "displayError('', '${msg}');"
            }
            out <<"""
                });
            </script>
                """

        }
    }

    private void printFieldErrors(def errors, def bean){
        String title = g.message(code: bean.class.name +".title.error")
        if (errors){
            out << """
            <script>
                \$(document).ready(function (){
                    displayError("", "${title}");
                 """
            errors.each{error ->
                String msg = tranlateErrorCode(error.codes)
                out << "appendErrorToField('${error.field}','${msg}');"
            }

            out <<"""
                });
            </script>
                """
        }

    }

    def validateForm = {attrs, body->
        def formId = attrs.form
        def className = attrs.command
        def bean = attrs.bean
        def obj

        if (!bean)
            obj = Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance()
        else{
            obj =  bean
            printGeneralErrors(obj.errors.allErrors - obj.errors.fieldErrors,obj);
            printFieldErrors(obj.errors.fieldErrors, obj)
        }



        def rules = new StringBuffer("rules: {")
        def message = new StringBuffer(" messages: {")
        out << """
		<script type="text/javascript">
			\$(function (){
				\$("#${formId}").validate({
                errorClass:'error',
                errorElement:'span',
"""

        obj.constraints.each{fields ->
            String fieldName = fields.key.toString()
            ConstrainedProperty constraint = fields.value
            if (constraint.appliedConstraints){

                if (grailsApplication.isDomainClass(constraint.propertyType)){
                    fieldName = "${fieldName}.id"
                }
                rules.append("'${fieldName}':{")
                message.append("'${fieldName}':{")
                constraint.appliedConstraints.each{c ->
                    printValidation(rules, message,c,fieldName)
                    printValidationType(rules, message,c, fieldName)
                }
                rules.deleteCharAt(rules.length() - 1);
                message.deleteCharAt(message.length() - 1);
                rules.append("},")
                message.append("},")
            }
        }
        rules.deleteCharAt(rules.length() - 1);
        message.deleteCharAt(message.length() - 1);
        rules.append("}")
        message.append("}")
        out <<
                """ ${rules} , ${message}
				});
			});
			</script>
			"""
    }
}
