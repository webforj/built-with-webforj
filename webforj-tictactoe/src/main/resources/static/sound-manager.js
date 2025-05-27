const audioFiles = ['click', 'win', 'lose', 'draw', 'tap'];
const audioElements = {};

audioFiles.forEach(file => {
  const audio = new Audio();
  audio.src = `/static/audio/${file}.mp3`;
  audio.id = `audio-${file}`;
  audio.preload = 'auto';
  audioElements[file] = audio;
});

document.addEventListener('click', () => {
  const clickAudio = audioElements['click'];
  clickAudio.play();
  clickAudio.pause();
  clickAudio.currentTime = 0;
}, { once: true });

window.audioElements = audioElements;
