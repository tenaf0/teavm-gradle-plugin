package hu.garaba;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.teavm.tooling.builder.InProcessBuildStrategy;

@Mapper
public interface ExtensionMapper {
	ExtensionMapper INSTANCE = Mappers.getMapper(ExtensionMapper.class);

	void builderFromExtension(TeaVMExtension settings, @MappingTarget InProcessBuildStrategy builder);
}
