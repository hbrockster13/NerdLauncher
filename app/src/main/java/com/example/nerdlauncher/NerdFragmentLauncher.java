package com.example.nerdlauncher;

import android.support.v4.app.Fragment;

public class NerdFragmentLauncher extends Fragment
{
    public static Fragment newInstance()
    {
        return new NerdFragmentLauncher();
    }
}
