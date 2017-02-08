package ${packageModelName};

import java.util.Date;

public class ${ModelName} {
	<#list fieds as f>
	private ${f.type} ${f.field};
	</#list>
}
