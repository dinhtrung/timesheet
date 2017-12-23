import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'findLanguageFromKey'})
export class FindLanguageFromKeyPipe implements PipeTransform {
    private languages: any = {
        'zh-cn': { name: '中文（简体）' },
        'nl': { name: 'Nederlands' },
        'en': { name: 'English' },
        'fr': { name: 'Français' },
        'de': { name: 'Deutsch' },
        'ja': { name: '日本語' },
        'ko': { name: '한국어' },
        'vi': { name: 'Tiếng Việt' }
        // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
    };
    transform(lang: string): string {
        return this.languages[lang].name;
    }
}
