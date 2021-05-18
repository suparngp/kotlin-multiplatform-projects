#!/usr/bin/env sh

# abort on errors
set -e

# build
yarn build

# navigate into the build output directory
cd docs/.vuepress/dist

# if you are deploying to a custom domain
echo 'kmpdocs.suparnatural.com' > CNAME

git init

git config --local user.email "hello@suparnatural.com"
git config --local user.username "suparngp"

git add -A
git commit -m 'deploy'

git push -f git@github.com:suparngp/kotlin-multiplatform-projects-site.git master:gh-pages
cd -