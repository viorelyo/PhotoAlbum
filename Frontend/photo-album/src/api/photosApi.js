const BASE_PATH = "http://localhost:8080/photoAlbum/photos";
const BASE_PATH_GET_PHOTOS = "http://localhost:8080/photoAlbum/photos/";

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
  const url = new URL(BASE_PATH_GET_PHOTOS);
  url.search = new URLSearchParams({
    albumId: albumId,
  });

  return fetch(url.toString())
    .then(response => response.json());
}

export const getAllPhotosByAlbumFilterAndSort = (albumId, from, to, ascending) => {
  const url = new URL(BASE_PATH + "/filterAndSort");
  url.search = new URLSearchParams({
    albumId: albumId,
  });
  if (from !== null && to != null) {
    url.searchParams.append("from", from);
    url.searchParams.append("to", to);
  }
  if (ascending !== null) {
    url.searchParams.append("asc", ascending);
  }

  return fetch(url.toString())
    .then(response => response.json());
}

export const deletePhotoById = (id) => {
  const url = new URL(BASE_PATH + "/remove");
  url.search = new URLSearchParams({
    photoId: id,
  });

  return fetch(url, {
    method: "DELETE",
  }).then((response) => response.status);
};
