export const paramsMaker = (object: { [key: string]: any }) => {
  let params = "?";
  for (const item in object) {
    params += item + "=" + object[item] + "&";
  }
  params = params.slice(0, params.length - 1);
  return params;
};
