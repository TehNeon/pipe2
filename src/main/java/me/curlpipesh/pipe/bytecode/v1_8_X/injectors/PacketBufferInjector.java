package me.curlpipesh.pipe.bytecode.v1_8_X.injectors;

import me.curlpipesh.pipe.bytecode.Injector;
import me.curlpipesh.pipe.bytecode.map.MappedClass;
import me.curlpipesh.pipe.bytecode.map.MappedClass.MethodDef;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.util.List;

/**
 * @author audrey
 * @since 12/15/15.
 */
public class PacketBufferInjector extends Injector {
    public PacketBufferInjector(final MappedClass classToInject) {
        super(classToInject);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void inject(final ClassReader classReader, final ClassNode classNode) {
        final MethodDef readStringFromBuffer = getClassToInject().getMethod("readStringFromBuffer").get();
        ((List<MethodNode>) classNode.methods).stream()
                .filter(m -> m.name.equals(readStringFromBuffer.getName()) &&
                        m.desc.contains(readStringFromBuffer.getDesc()))
                .forEach(m -> {
                    //Pipe.getLogger().info("Found PacketBuffer#readStringFromBuffer():" + m.name + " : " + m.desc);
                    final InsnList list = new InsnList();
                    list.add(new MethodInsnNode(INVOKESTATIC,
                            "me/curlpipesh/pipe/Pipe", "getInstance", "()Lme/curlpipesh/pipe/Pipe;", false));
                    list.add(new MethodInsnNode(INVOKEVIRTUAL, "me/curlpipesh/pipe/Pipe", "getEventBus", "()Lme/curlpipesh/pipe/event/EventBus;", false));
                    list.add(new TypeInsnNode(NEW, "me/curlpipesh/pipe/event/events/PacketBufferStringRead"));
                    list.add(new InsnNode(DUP));
                    list.add(new VarInsnNode(ALOAD, 3));
                    list.add(new MethodInsnNode(INVOKESPECIAL, "me/curlpipesh/pipe/event/events/PacketBufferStringRead", "<init>", "(Ljava/lang/String;)V", false));
                    list.add(new MethodInsnNode(INVOKEINTERFACE, "me/curlpipesh/pipe/event/EventBus", "push", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
                    list.add(new InsnNode(POP));
                    m.instructions.insertBefore(m.instructions.getLast().getPrevious(), list);
                });
    }
}
