
const BASE_PATH = "http://localhost:8080/photoAlbum/albums";

export const getAllAlbums = () => {
  const url = new URL(BASE_PATH);

  return fetch(url.toString())
    .then(response => response.json());
}

export const addAlbum = (albumName) => {
  const url = new URL(BASE_PATH + "/add");
  url.search = new URLSearchParams({
    albumName: albumName,
  });

  return fetch(url, {
    method: "POST"
  }).then((response) => response.status);
};
