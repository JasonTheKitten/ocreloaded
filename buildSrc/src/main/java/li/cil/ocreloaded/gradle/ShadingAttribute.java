package li.cil.ocreloaded.gradle;

import org.gradle.api.Named;
import org.gradle.api.attributes.Attribute;

public interface ShadingAttribute extends Named {
	Attribute<ShadingAttribute> SHADING_ATTRIBUTE = Attribute.of(ShadingAttribute.class);

	String SHADED = "shaded";
	String STANDARD = "standard";
}