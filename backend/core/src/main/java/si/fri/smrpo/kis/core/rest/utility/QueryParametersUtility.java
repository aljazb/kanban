package si.fri.smrpo.kis.core.rest.utility;

import com.github.tfaga.lynx.beans.QueryFilter;

import java.util.List;
import java.util.stream.Collectors;

public class QueryParametersUtility {

    public static void addParam(List<QueryFilter> queryFilterList, QueryFilter queryFilter){
        List<QueryFilter> filters = queryFilterList.stream()
                .filter(e -> e.getField().equals(queryFilter.getField()))
                .collect(Collectors.toList());

        queryFilterList.removeAll(filters);
        queryFilterList.add(queryFilter);
    }

}
