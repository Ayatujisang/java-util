package ${packageName};

import java.util.List;
import java.util.Map;


public interface ${ModelName}Mapper {

	public void insert(${ModelName} ${modelName});

	public ${ModelName} getById(int id);

	public void updateById(${ModelName} ${modelName});

	public void deleteById(int id);
}
