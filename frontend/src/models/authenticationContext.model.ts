export type AuthenticationContextype = {
  token: string;
  setToken: (token: string) => void;
  role: string;
  setRole: (role: string) => void;
  fetchdataParams: (
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    params: { [key: string]: any }
  ) => Promise<any>;
  fetchdataBody: (
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    body: BodyInit
  ) => Promise<any>;
  fetchdataBoth: (
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE",
    body: BodyInit,
    params: { [key: string]: any }
  ) => Promise<any>;
};
