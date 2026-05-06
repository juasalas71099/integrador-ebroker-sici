package com.cmscomunidades.CMS.base.jpa;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

@Data
@Builder
public class SearchCriteria {

	public static final String OPERATOR_AND = "[AND]";
	public static final String OPERATOR_OR = "[OR]";
	public static final ImmutableList<String> PAGINATION_SORT_PARAMS = ImmutableList.of("page", "size", "sort");

	private String key;
	private List<MatchCriteria> matchCriterias;
	private String operator;

	public static List<SearchCriteria> fromQueryPath(MultiValueMap<String, String> queryPath) {
		List<SearchCriteria> criterias = new ArrayList<>();

		for (Entry<String, List<String>> entry : queryPath.entrySet()) {
			String key = entry.getKey();
			List<String> valuesStr = entry.getValue();
			// En el queryPath llegarán los parámetros de paginación y orden
			// deberemos descartarlos para no generar predicados para filtrar
			if(PAGINATION_SORT_PARAMS.contains(key)) {
				continue;
			}
			String operator = valuesStr.contains(OPERATOR_AND) ? OPERATOR_AND : OPERATOR_OR;
			valuesStr.remove(OPERATOR_AND);
			valuesStr.remove(OPERATOR_OR);
			List<MatchCriteria> matchCriterias = new ArrayList<>();

			valuesStr.stream().forEach(valueStr -> {
				MatchMode matchMode = MatchMode.fromString(valueStr);
				Object value = MatchMode.clearValue(valueStr);
				matchCriterias.add(MatchCriteria.builder().value(value).matchMode(matchMode).build());
			});

			criterias.add(SearchCriteria.builder().key(key).operator(operator).matchCriterias(matchCriterias).build());
		}
		return criterias;
	}
}
