package li.cil.ocreloaded.minecraft.client.sound;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.CompletableFuture;

import javax.sound.sampled.AudioFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.SampledFloat;

public class BeepSound extends AbstractSoundInstance implements AudioStream {

    private static final int SAMPLE_RATE = 4000;

    private final ByteBuffer audioBuffer;

    public BeepSound(ResourceLocation resourceLocation, BlockPos position, short frequency, short duration) {
        super(resourceLocation, SoundSource.BLOCKS, SoundInstance.createUnseededRandom());
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
        this.audioBuffer = createAudioBuffer(frequency, duration);
    }

    @Override
    public void close() throws IOException {
        audioBuffer.clear();
    }

    @Override
    public AudioFormat getFormat() {
        return new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
    }

    @Override
    public ByteBuffer read(int capacity) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.LITTLE_ENDIAN);
        int remaining = Math.min(capacity, audioBuffer.remaining());
        byte[] bytes = new byte[remaining];
        audioBuffer.get(bytes);
        buffer.put(bytes);
        buffer.flip();
        return buffer;
    }

    @Override
    public WeighedSoundEvents resolve(SoundManager soundManager) {
        float volume = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.BLOCKS);
        this.sound = new Sound(
            new ResourceLocation("minecraft", "intentionally_empty").toString(),
            (SampledFloat) ConstantFloat.of(volume), (SampledFloat) ConstantFloat.of(1.0f), 1, Sound.Type.FILE, true, false, 16);
        return new WeighedSoundEvents(getLocation(), null);
    }

    // These are non-annotated overrides (it's not possible to annotate them)
    public CompletableFuture<AudioStream> getAudioStream(SoundBufferLibrary loader, ResourceLocation id, boolean repeatInstantly) {
        return CompletableFuture.completedFuture(this);
    }

    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary loader, ResourceLocation id, boolean repeatInstantly) {
        return CompletableFuture.completedFuture(this);
    }
    // End overrides

    private static ByteBuffer createAudioBuffer(int frequency, int duration) {
        int numSamples = SAMPLE_RATE * duration / 1000;
        ByteBuffer buffer = ByteBuffer.allocateDirect(2 * numSamples).order(ByteOrder.LITTLE_ENDIAN);
        int period = SAMPLE_RATE / frequency;
        short amplitude = Short.MAX_VALUE;
        for (int i = 0; i < numSamples; i++) {
            buffer.putShort((short) ((i % period < period / 2) ? amplitude : -amplitude));
        }
        buffer.flip();
        return buffer;
    }
    
}
