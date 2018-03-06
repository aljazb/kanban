package si.fri.smrpo.kis.api.client.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QueryParamBuilder {

    public final String AND = "%7C"; //'|';
    public final String OR = "%2C"; //',';

    public final String LIKE = ":like:";
    public final String LIKEIC = ":likeic:";
    public final String EQ = ":eq:";
    public final String GEQ = ":gte:";
    public final String LEQ = ":lte:";
    public final String IS_NULL = ":isnull";
    public final String IS_NOT_NULL = ":isnotnull";

    public final String E_MOD = "%25"; // '%'
    public final String E_AND = "%26"; // '&'
    public final String E_WS = "%20"; // ' '

    public final String PAR = "'";

    final static public SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public enum OrderByType {
        ASC, DESC
    }

    public enum CompOperator {
        EQ, GEQ, LEQ
    }

    private List<String> defaultSelect;
    private List<String> select;

    private List<String> defaultWhereConditions;
    private List<String> whereConditions;

    private String defaultOrderBy;
    private String orderBy;

    private OrderByType defaultOrderByType;
    private OrderByType orderByType;

    private int defaultLimit = 30;
    private int limit = 30;

    private int defaultSkip = 0;
    private int skip = 0;

    private Boolean defaultIsDeleted = false;
    private Boolean defaultIsLatest = null;

    public  QueryParamBuilder(){
        defaultWhereConditions = new ArrayList<>();
        whereConditions = new ArrayList<>();
        defaultSelect = new ArrayList<>();
        select = new ArrayList<>();
        orderBy = null;
        orderByType = null;
    }

    public void clearSelection(){
        select.clear();
        select.addAll(defaultSelect);
    }

    public void clearConditions(){
        whereConditions.clear();

        orderBy = defaultOrderBy;
        orderByType = defaultOrderByType;

        whereConditions.addAll(defaultWhereConditions);
    }

    public void clearPaging(){
        limit = defaultLimit;
        skip = defaultSkip;
    }

    public void addSelect(String selectFieldName) {
        addSelect(selectFieldName, false);
    }

    public void addSelect(String selectFieldName, boolean isDefault){
        if(isDefault){
            defaultSelect.add(selectFieldName);
        } else {
            select.add(selectFieldName);
        }
    }

    public void addCond(String cond){
        addCond(cond, false);
    }

    public void addCond(String cond, boolean isDefault){
        if(isDefault){
            defaultWhereConditions.add(cond);
        } else {
            whereConditions.add(cond);
        }
    }

    public void addOpCond(String field, String value, CompOperator compOperator){
        addOpCond(field, value, compOperator, false);
    }

    public void addOpCond(String field, String value, CompOperator compOperator, boolean isDefault){
        String op;
        switch (compOperator){
            case EQ:
                op = EQ;
                break;
            case GEQ:
                op = GEQ;
                break;
            case LEQ:
                op = LEQ;
                break;
            default:
                op = EQ;
                break;
        }

        addCond(field + op + value, isDefault);
    }

    public void addLikeCond(String field, String value){
        addLikeCond(field, value, true, false, false);
    }

    public void addLikeCond(String field, String value, boolean ignoreCase){
        addLikeCond(field, value, ignoreCase, false, false);
    }

    public void addLikeCond(String field, String value, boolean ignoreCase, boolean exactValue, boolean isDefault){
        value = filterStringForUrl(value);

        String uep = E_MOD;
        if(exactValue){
            uep = "";
        }

        if(ignoreCase){
            addCond(field + LIKEIC + PAR + uep + value + uep + PAR, isDefault);
        } else {
            addCond(field + LIKE + PAR + uep + value + uep + PAR, isDefault);
        }
    }

    public void addDateCond(String field, String value, CompOperator compOperator) throws ParseException {
        addDateCond(field, value, compOperator, false);
    }

    public void addDateCond(String field, String value, CompOperator compOperator, boolean isDefault) throws ParseException {
        value = filterStringForUrl(value);

        if(compOperator == CompOperator.EQ){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(value));

            ZonedDateTime zonedDateTimeAfter = ZonedDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
            String dateQueryAfter = "dt'" + zonedDateTimeAfter.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) + "'";
            String condAfter = field + GEQ + dateQueryAfter;
            addCond(condAfter, isDefault);

            calendar.add(Calendar.DATE, 1);
            ZonedDateTime zonedDateTimeBefore = ZonedDateTime.ofInstant(calendar.getTime().toInstant(), ZoneId.systemDefault());
            String dateQueryBefore = "dt'" + zonedDateTimeBefore.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) + "'";
            String condBefore = field + LEQ + dateQueryBefore;
            addCond(condBefore, isDefault);
        } else {
            Date date = sdf.parse(value);

            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            String dateQuery = "dt'" + zonedDateTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) + "'";

            String op = compOperator == CompOperator.GEQ ? GEQ : LEQ;
            String cond = field + op + dateQuery;
            addCond(cond, isDefault);
        }
    }

    public void addEqCond(String field, String value){
        addEqCond(field, value, false);
    }

    public void addEqCond(String field, String value, boolean isDefault){
        value = filterStringForUrl(value);
        addCond(field + EQ + value, isDefault);
    }

    public void addIsNullCond(String field){
        addIsNullCond(field + IS_NULL, false);
    }

    public void addIsNotNullCond(String field){
        addIsNullCond(field + IS_NOT_NULL, false);
    }

    public void addIsNullCond(String field, boolean isDefault){
        addCond(field + IS_NULL, isDefault);
    }

    private String filterStringForUrl(String value){
        return value.replaceAll(" ", E_WS).replaceAll("&", E_AND);
    }

    public String buildQuery(){
        if(defaultIsDeleted != null){
            addEqCond("isDeleted", defaultIsDeleted.toString());
        }
        if(defaultIsLatest != null){
            addEqCond("isLatest", defaultIsLatest.toString());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("?");
        sb.append("limit=" + limit);
        sb.append("&");
        sb.append("skip=" + skip);

        if(!select.isEmpty()){
            sb.append("&select=");
            sb.append(select.get(0));

            for(int i=1; i<select.size(); i++){
                sb.append(",");
                sb.append(select.get(i));
            }
        }

        if(!whereConditions.isEmpty()){
            sb.append("&where=");
            sb.append(whereConditions.get(0));

            for(int i=1; i<whereConditions.size(); i++){
                sb.append(AND);
                sb.append(whereConditions.get(i));
            }
        }

        if(orderBy != null){
            sb.append("&order=" + orderBy);
            if(orderByType == null || orderByType == OrderByType.ASC)
                sb.append("+ASC");
            else
                sb.append("+DESC");
        }

        return sb.toString();
    }

    public void orderBy(String property, OrderByType orderByType){
        orderBy(property, orderByType, false);
    }

    public void orderBy(String property, OrderByType orderByType, boolean isDefault){
        if(isDefault){
            this.defaultOrderBy = property;
            this.defaultOrderByType = orderByType;
        } else {
            this.orderBy = property;
            this.orderByType = orderByType;
        }
    }

    public void setPage(int page){
        skip = (page -1) * limit;
    }

    public void setLimit(int limit){
        setLimit(limit, false);
    }

    public void setLimit(int limit, boolean isDefault){
        if(isDefault){
            this.defaultLimit = limit;
        } else {
            this.limit = limit;
        }
    }

    public void setSkip (int skip){
        this.skip = skip;
    }

    public void setSkip (int skip, boolean isDefault){
        if(isDefault){
            this.defaultSkip = skip;
        } else {
            this.skip = skip;
        }
    }

    public int getLimit(){
        return limit;
    }

    public int getSkip(){
        return skip;
    }

    public Boolean getDefaultIsDeleted() {
        return defaultIsDeleted;
    }

    public void setDefaultIsDeleted(Boolean defaultIsDeleted) {
        this.defaultIsDeleted = defaultIsDeleted;
    }

    public Boolean getDefaultIsLatest() {
        return defaultIsLatest;
    }

    public void setDefaultIsLatest(Boolean defaultIsLatest) {
        this.defaultIsLatest = defaultIsLatest;
    }
}
