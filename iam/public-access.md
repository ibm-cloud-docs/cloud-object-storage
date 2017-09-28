---

copyright:
  years: 2017
lastupdated: "2017-09-27"

---

# Allowing anonymous access

To make an object publicly accessible, provide an `x-amz-acl: public-read` header when uploading the object.

## Upload a public object
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "x-amz-acl: public-read" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"

## Make a public object private again
curl -X "PUT" "https://{endpoint}/{bucket-name}/{object-name}" \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: text/plain; charset=utf-8" \
     -d "{object-contents}"
