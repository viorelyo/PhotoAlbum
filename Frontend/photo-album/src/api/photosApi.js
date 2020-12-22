const BASE_PATH = "http://localhost:8080/photoAlbum/photos";
const BASE_PATH_GET_PHOTOS = "http://localhost:8080/photoAlbum/photos/getphotosbyalbum";
const BASE_PATH_GET_BINARIES = "http://localhost:8080/photoAlbum/photos/downloadByAlbum";

export const getPhotoById = (id) => {
  const url = new URL(BASE_PATH + "/download");
  url.search = new URLSearchParams({
    photoId: id,
  });

  return fetch(url).then((response) => response.blob());
};

export const uploadPhoto = (file, albumId) => {
  const url = new URL(BASE_PATH + "/upload");

  const formData = new FormData();
  formData.append("file", file);
  formData.append("albumId", albumId);

  return fetch(url, {
    method: "POST",
    body: formData,
  }).then((response) => response.status);
};

export const getAllPhotosByAlbum = (albumId) => {
  const url = new URL(BASE_PATH_GET_PHOTOS+ "?albumId="+albumId);

  return fetch(url.toString())
    .then(response => response.json());
}

export const getAllPhotosBinariesByAlbum = (albumId) => {
  const url = new URL(BASE_PATH_GET_BINARIES+ "?albumId="+albumId);

  return fetch(url.toString())
    .then(response => response.json());
}