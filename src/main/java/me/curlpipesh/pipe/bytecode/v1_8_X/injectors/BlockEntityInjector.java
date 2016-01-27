package me.curlpipesh.pipe.bytecode.v1_8_X.injectors;

import me.curlpipesh.pipe.bytecode.Injector;
import me.curlpipesh.pipe.bytecode.map.MappedClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.List;

/**
 * Makes the position of BlockEntities accessible
 *
 * @author c
 * @since 5/23/15
 */
public class BlockEntityInjector extends Injector {
    public BlockEntityInjector(final MappedClass classToInject) {
        super(classToInject);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void inject(final ClassReader classReader, final ClassNode classNode) {
        final String blockPos = getClassToInject().getFields().get("blockPos");
        ((List<FieldNode>) classNode.fields).stream().filter(f -> f.name.equals(blockPos))
                .forEach(f -> f.access = ACC_PUBLIC);
    }
}
