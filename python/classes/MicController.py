import pyaudio
import numpy as np

p = pyaudio.PyAudio()
RATE = 44100
CHUNK = 2048

maxLev = {
    "Sub Bass": 15000,
    "Bass":     15000,
    "Mid":      5000,
    "Treble":   750,
    "Air":      300,
}

stream = p.open(format=pyaudio.paInt16,
                channels=1,
                rate=44100,
                input=True,
                frames_per_buffer=CHUNK,
                input_device_index=0)

def get_band_level(data, freq, low, high):
    mask = (freq >= low) & (freq < high)
    return np.mean(np.abs(data[mask]))

def getAudio():
    data = stream.read(CHUNK, exception_on_overflow=False)
    samples = np.frombuffer(data, dtype=np.int16).astype(float)
    fft = np.fft.rfft(samples)
    freqs = np.fft.rfftfreq(CHUNK, 1/RATE)
    bands = {
        "Sub Bass": get_band_level(fft, freqs, 20, 60)    / maxLev["Sub Bass"],
        "Bass":     get_band_level(fft, freqs, 60, 250)   / maxLev["Bass"],
        "Mid":      get_band_level(fft, freqs, 250, 2000) / maxLev["Mid"],
        "Treble":   get_band_level(fft, freqs, 2000, 8000)/ maxLev["Treble"],
        "Air":      get_band_level(fft, freqs, 8000, 20000)/maxLev["Air"],
    }
    return bands

def getVolume():
    data = stream.read(CHUNK, exception_on_overflow=False)
    samples = np.frombuffer(data, dtype=np.int16).astype(float)
    rms = np.sqrt(np.mean(samples**2))
    return round((rms / 32767) * 100, 1)
