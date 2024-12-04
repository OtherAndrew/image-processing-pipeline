#!/usr/bin/env bash

cd "$(dirname "$0")" || exit 1

rm package.zip

pushd venv/lib/python3.12/site-packages
zip -r ../../../../package.zip .

popd

zip package.zip *.py